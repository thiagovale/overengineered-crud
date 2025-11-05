import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import {
  ClientRequest,
  ClientResponse,
  SuccessResponse,
} from '../../shared/models';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private readonly endpoint = '/client';

  constructor(private apiService: ApiService) {}

  getClients(): Observable<SuccessResponse<ClientResponse[]>> {
    return this.apiService.get<SuccessResponse<ClientResponse[]>>(
      this.endpoint
    );
  }

  getClientById(id: number): Observable<SuccessResponse<ClientResponse>> {
    return this.apiService.get<SuccessResponse<ClientResponse>>(
      `${this.endpoint}/${id}`
    );
  }

  createClient(
    client: ClientRequest
  ): Observable<SuccessResponse<ClientResponse>> {
    return this.apiService.post<SuccessResponse<ClientResponse>>(
      this.endpoint,
      client
    );
  }

  updateClient(
    id: number,
    client: ClientRequest
  ): Observable<SuccessResponse<ClientResponse>> {
    return this.apiService.put<SuccessResponse<ClientResponse>>(
      `${this.endpoint}/${id}`,
      client
    );
  }

  deleteClient(id: number): Observable<SuccessResponse<void>> {
    return this.apiService.delete<SuccessResponse<void>>(
      `${this.endpoint}/${id}`
    );
  }
}
