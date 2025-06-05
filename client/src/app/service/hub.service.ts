import { Injectable, signal } from '@angular/core';
import { Client, Stomp, StompHeaders } from '@stomp/stompjs'
import { LocalStorageService } from 'ngx-webstorage';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import { WebsiteResponse } from '../model/website.type';
import { TicksRequest } from '../model/ticks.type';

@Injectable({
  providedIn: 'root'
})
export class HubService {
  stompClient: Client | null = null;

  validateRequest = new Subject<WebsiteResponse>();
  connected = new Subject<boolean>();

  constructor(private localstore: LocalStorageService) {
    this.connected.next(false);
  }

  connect() {
    const socket = new SockJS('http://localhost:8080/ws-hub?token=' + this.localstore.retrieve('accessToken'));

    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    this.stompClient.onConnect = (frame) => {
      this.connected.next(true);

      this.stompClient?.subscribe('/topic/public', (message) => {
        this.validateRequest.next(JSON.parse(message.body));
      });
    }

    this.stompClient.onStompError = (frame) => {
      console.log(frame.body);
    }

    this.stompClient?.activate();
  }

  sendMessage(msg: TicksRequest) {
    msg.validator = this.localstore.retrieve('username');
    
    if(this.stompClient && this.stompClient.connected) {
      this.stompClient?.publish({
        destination: '/app/generate-ticks',
        body: JSON.stringify(msg)
      });
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.connected.next(false);
    }
  }
}
