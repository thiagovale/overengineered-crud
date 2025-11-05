import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ClientTableComponent } from './shared/components/client-table/client-table.component';
import { ClientFormComponent } from './shared/components/client-form/client-form.component';
import { ClientDetailModalComponent } from './shared/components/client-detail-modal/client-detail-modal.component';
import { ButtonComponent } from './shared/components/button/button.component';
import { ClientResponse, ClientRequest } from './shared/models';
import { ClientService } from './core/services/client.service';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-clients',
  imports: [
    CommonModule,
    ClientTableComponent,
    ClientFormComponent,
    ClientDetailModalComponent,
    ButtonComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  title = 'frontend';

  clients: ClientResponse[] = [];
  showForm = false;
  selectedClient: ClientRequest | undefined;
  selectedClientId: number | undefined;
  showDetailModal = false;
  clientDetail: ClientResponse | null = null;

  constructor(
    private clientService: ClientService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.clientService.getClients().subscribe({
      next: (response) => {
        this.clients = response.data.sort((a, b) => a.id - b.id);
        console.log('Clientes carregados:', this.clients);
      },
      error: (error) => {
        console.error('Erro ao carregar clientes:', error);
      },
    });
  }

  onEdit(client: ClientResponse) {
    this.selectedClient = {
      firstName: client.firstName,
      lastName: client.lastName,
      email: client.email,
      documentNumber: client.documentNumber,
      dateOfBirth: client.dateOfBirth,
      addresses: client.addresses,
      phoneNumbers: client.phoneNumbers,
    };
    this.selectedClientId = client.id;
    this.showForm = true;
  }

  onDelete(clientId: number) {
    if (confirm('Tem certeza que deseja deletar este cliente?')) {
      this.clientService.deleteClient(clientId).subscribe({
        next: (response) => {
          console.log('Cliente deletado:', response.message);
          this.loadClients();
        },
        error: (error) => {
          console.error('Erro ao deletar cliente:', error);
        },
      });
    }
  }

  onFormSubmit(clientData: ClientRequest) {
    if (this.selectedClientId) {
      // Atualizar cliente existente
      this.clientService
        .updateClient(this.selectedClientId, clientData)
        .subscribe({
          next: (response) => {
            console.log('Cliente atualizado:', response.message);
            this.loadClients();
            this.resetForm();
          },
          error: (error) => {
            console.error('Erro ao atualizar cliente:', error);
          },
        });
    } else {
      // Criar novo cliente
      this.clientService.createClient(clientData).subscribe({
        next: (response) => {
          console.log('Cliente criado:', response.message);
          this.loadClients();
          this.resetForm();
        },
        error: (error) => {
          console.error('Erro ao criar cliente:', error);
        },
      });
    }
  }

  onFormCancel() {
    this.resetForm();
  }

  resetForm() {
    this.selectedClient = undefined;
    this.selectedClientId = undefined;
    this.showForm = false;
  }

  toggleForm() {
    if (this.showForm) {
      this.selectedClient = undefined;
    }
    this.showForm = !this.showForm;
  }

  onRowClick(clientId: number) {
    this.clientService.getClientById(clientId).subscribe({
      next: (response) => {
        this.clientDetail = response.data;
        this.showDetailModal = true;
      },
      error: (error) => {
        console.error('Erro ao carregar detalhes do cliente:', error);
      },
    });
  }

  onCloseModal() {
    this.showDetailModal = false;
    this.clientDetail = null;
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
