export interface TicksDto {
  status: Status;
  websiteId: number;
  validator: string;
}

export type Status = 'UP' | 'DOWN';
