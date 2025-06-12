import { Status, TicksResponse } from "./ticks.type";

export interface WebsiteResponse {
  readonly id: number;
  url: string;
}

export interface WebsiteRequest {
  url: string
}

export interface WebSiteDto {
  readonly id: number,
  url: string,
  ticks: TicksResponse[]
}
