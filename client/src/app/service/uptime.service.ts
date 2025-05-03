import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UptimeService {
  constructor() {}

  async getUptime(url: string) {
    const response = await fetch(`${url}`, {method: 'HEAD', mode: 'no-cors'});
    return response;
  }
}
