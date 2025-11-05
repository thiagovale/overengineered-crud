import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../button/button.component';
import { ClientResponse } from '../../models';

@Component({
  selector: 'app-client-table',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './client-table.component.html',
  styleUrl: './client-table.component.css',
})
export class ClientTableComponent {
  @Input() clients: ClientResponse[] = [];
  @Output() edit = new EventEmitter<ClientResponse>();
  @Output() delete = new EventEmitter<number>();
  @Output() rowClick = new EventEmitter<number>();

  onEdit(client: ClientResponse) {
    this.edit.emit(client);
  }

  onRowClick(clientId: number) {
    this.rowClick.emit(clientId);
  }

  onDelete(id: number) {
    this.delete.emit(id);
  }
}
