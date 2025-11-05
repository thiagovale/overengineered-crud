import { Address, AddressRequest, AddressResponse } from './address.model';
import {
  PhoneNumber,
  PhoneNumberRequest,
  PhoneNumberResponse,
} from './phone-number.model';

export interface Client {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  documentNumber: string;
  dateOfBirth: string;
  addresses: Address[];
  phoneNumbers: PhoneNumber[];
}

export interface ClientRequest {
  firstName: string;
  lastName: string;
  email: string;
  documentNumber: string;
  dateOfBirth: string;
  addresses: AddressRequest[];
  phoneNumbers: PhoneNumberRequest[];
}

export interface ClientResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  documentNumber: string;
  dateOfBirth: string;
  addresses: AddressResponse[];
  phoneNumbers: PhoneNumberResponse[];
}
