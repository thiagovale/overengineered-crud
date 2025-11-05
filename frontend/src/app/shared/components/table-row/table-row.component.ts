import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../button/button.component';
import { ClientResponse } from '../../models';

@Component({
  selector: 'app-table-row',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './table-row.component.html',
  styleUrl: './table-row.component.css',
})
export class TableRowComponent {
  @Input() client!: ClientResponse;
  @Output() edit = new EventEmitter<ClientResponse>();
  @Output() delete = new EventEmitter<number>();

  onEdit() {
    this.edit.emit(this.client);
  }

  onDelete() {
    if (
      confirm(
        `Deseja realmente deletar ${this.client.firstName} ${this.client.lastName}?`
      )
    ) {
      this.delete.emit(this.client.id);
    }
  }
}
