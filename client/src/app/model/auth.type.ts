export interface RegistrationRequest {
  email: string;
  password: string;
  role: Role;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  email: string;
  role: Role;
  accessToken: string;
  refreshToken: string;
}

export type Role = 'User' | 'Validator';

