export interface Address {
  id?: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface AddressRequest {
  street: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface AddressResponse {
  id: number;
  street: string;
  city: string;
  state: string;
  zipCode: string;
}