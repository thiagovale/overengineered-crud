export interface SuccessResponse<T> {
  data: T;
  message?: string;
  traceId: string;
  timestamp: string;
}

export interface ErrorResponse {
  message: string;
  error: string;
  traceId: string;
  timestamp: string;
}

export interface ValidationErrorResponse {
  message: string;
  errors: Record<string, string[]>;
  traceId: string;
  timestamp: string;
}