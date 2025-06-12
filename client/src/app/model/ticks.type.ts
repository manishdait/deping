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
  fromTimestamp: Date,
  toTimestamp: Date,
  websiteId: number;
}

export type Status = 'UP' | 'DOWN' | 'UNKNOWN';
