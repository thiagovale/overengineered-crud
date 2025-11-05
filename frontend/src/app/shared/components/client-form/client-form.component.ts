import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormArray,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { InputComponent } from '../input/input.component';
import { ButtonComponent } from '../button/button.component';
import { ClientRequest } from '../../models';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputComponent, ButtonComponent],
  templateUrl: './client-form.component.html',
  styleUrl: './client-form.component.css',
})
export class ClientFormComponent implements OnInit {
  @Input() initialData?: ClientRequest;
  @Output() formSubmit = new EventEmitter<ClientRequest>();
  @Output() formCancel = new EventEmitter<void>();

  clientForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.clientForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      documentNumber: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
      addresses: this.fb.array([]),
      phoneNumbers: this.fb.array([]),
    });
  }

  ngOnInit() {
    if (this.initialData) {
      this.clientForm.patchValue({
        firstName: this.initialData.firstName,
        lastName: this.initialData.lastName,
        email: this.initialData.email,
        documentNumber: this.initialData.documentNumber,
        dateOfBirth: this.initialData.dateOfBirth,
      });

      // Adicionar endereços existentes
      this.initialData.addresses.forEach((address) => {
        this.addresses.push(
          this.fb.group({
            street: [address.street, [Validators.required]],
            city: [address.city, [Validators.required]],
            state: [address.state, [Validators.required]],
            zipCode: [address.zipCode, [Validators.required]],
          })
        );
      });

      // Adicionar telefones existentes
      this.initialData.phoneNumbers.forEach((phone) => {
        this.phoneNumbers.push(
          this.fb.group({
            number: [phone.number, [Validators.required]],
            type: [phone.type, [Validators.required]],
          })
        );
      });
    }
  }

  get addresses(): FormArray {
    return this.clientForm.get('addresses') as FormArray;
  }

  get phoneNumbers(): FormArray {
    return this.clientForm.get('phoneNumbers') as FormArray;
  }

  addAddress() {
    this.addresses.push(
      this.fb.group({
        street: ['', [Validators.required]],
        city: ['', [Validators.required]],
        state: ['', [Validators.required]],
        zipCode: ['', [Validators.required]],
      })
    );
  }

  removeAddress(index: number) {
    this.addresses.removeAt(index);
  }

  addPhoneNumber() {
    this.phoneNumbers.push(
      this.fb.group({
        number: ['', [Validators.required]],
        type: ['mobile', [Validators.required]],
      })
    );
  }

  removePhoneNumber(index: number) {
    this.phoneNumbers.removeAt(index);
  }

  onSubmit() {
    if (this.clientForm.valid) {
      const formData: ClientRequest = this.clientForm.value;
      this.formSubmit.emit(formData);
    }
  }

  onCancel() {
    this.formCancel.emit();
  }

  getErrorMessage(fieldName: string): string {
    const field = this.clientForm.get(fieldName);
    if (field?.hasError('required')) {
      return 'Campo obrigatório';
    }
    if (field?.hasError('email')) {
      return 'Email inválido';
    }
    return '';
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.clientForm.get(fieldName);
    return !!(field?.invalid && field?.touched);
  }
}
