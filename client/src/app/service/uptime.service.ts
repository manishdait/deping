import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UptimeService {
  constructor() {}

  async getUptime(url: string) {
    // temp fix to avoid cross need to find better solution
    const response = await fetch(`${url}`, {method: 'HEAD', mode: 'no-cors'});
    return response;
  }
}
