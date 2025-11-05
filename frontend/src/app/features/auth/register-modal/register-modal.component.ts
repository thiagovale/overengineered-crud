import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { InputComponent } from '../../../shared/components/input/input.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';

@Component({
  selector: 'app-register-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputComponent, ButtonComponent],
  templateUrl: './register-modal.component.html',
  styleUrl: './register-modal.component.css',
})
export class RegisterModalComponent {
  @Output() close = new EventEmitter<void>();
  @Output() registered = new EventEmitter<void>();

  registerForm: FormGroup;
  errorMessage = '';
  isLoading = false;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  onSubmit() {
    // Marca todos os campos como touched para mostrar erros
    Object.keys(this.registerForm.controls).forEach((key) => {
      this.registerForm.get(key)?.markAsTouched();
    });

    // Valida se as senhas conferem
    if (
      this.registerForm.get('password')?.value !==
      this.registerForm.get('confirmPassword')?.value
    ) {
      this.errorMessage = 'As senhas não conferem';
      return;
    }

    if (this.registerForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const { username, password } = this.registerForm.value;

      this.authService.register({ username, password }).subscribe({
        next: () => {
          this.registered.emit();
          this.close.emit();
        },
        error: (error) => {
          this.errorMessage = error.message || 'Erro ao criar conta';
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        },
      });
    }
  }

  onBackdropClick(event: MouseEvent) {
    if (event.target === event.currentTarget) {
      this.close.emit();
    }
  }

  onCancel() {
    this.close.emit();
  }

  getErrorMessage(fieldName: string): string {
    const field = this.registerForm.get(fieldName);

    // Só mostra erro se o campo foi tocado
    if (!field?.touched) {
      return '';
    }

    if (field?.hasError('required')) {
      return 'Campo obrigatório';
    }
    if (field?.hasError('minlength')) {
      const minLength = field.getError('minlength').requiredLength;
      return `Mínimo de ${minLength} caracteres`;
    }
    return '';
  }
}
