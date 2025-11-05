export interface PhoneNumber {
  id?: number;
  number: string;
  type: PhoneType;
}

export interface PhoneNumberRequest {
  number: string;
  type: PhoneType;
}

export interface PhoneNumberResponse {
  id: number;
  number: string;
  type: PhoneType;
}

export type PhoneType = 'mobile' | 'home' | 'work';