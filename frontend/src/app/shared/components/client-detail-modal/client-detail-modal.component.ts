import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientResponse } from '../../models';
import { ButtonComponent } from '../button/button.component';

@Component({
  selector: 'app-client-detail-modal',
  standalone: true,
  imports: [CommonModule, ButtonComponent],
  templateUrl: './client-detail-modal.component.html',
  styleUrl: './client-detail-modal.component.css',
})
export class ClientDetailModalComponent {
  @Input() client: ClientResponse | null = null;
  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }

  onBackdropClick(event: MouseEvent) {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }
}
