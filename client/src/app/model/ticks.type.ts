export interface TicksRequest {
  status: Status;
  websiteId: number;
  validator: string;
}

export interface TicksResponse {
  status: Status;
  avgUp: number;
  avgDown: number,
  up: number,
  down: number,
  websiteId: number;
}

export type Status = 'UP' | 'DOWN';
