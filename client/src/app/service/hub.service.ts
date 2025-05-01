import { Injectable, signal } from '@angular/core';
import { Client, Stomp, StompHeaders } from '@stomp/stompjs'
import { LocalStorageService } from 'ngx-webstorage';
import { Subject } from 'rxjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class HubService {
  stompClient: Client | null = null;
  connected = new Subject<boolean>();

  constructor(private localstore: LocalStorageService) {
    this.connected.next(false);
  }

  connect() {
    console.log('Bearer ' + this.localstore.retrieve('accessToken'));
    
    const socket = new SockJS('http://localhost:8080/ws-hub');

    this.stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (str) => console.log(str)
    });

    this.stompClient.onConnect = (frame) => {
      this.connected.next(true);

      this.stompClient?.subscribe('/topic/public', (message) => {
        console.log(message.body);
      });

      this.stompClient?.publish({
        destination: '/app/tbd',
        body: JSON.stringify({ url: 'JOin' })
      });
    }

    this.stompClient.onStompError = (frame) => {
      console.log(frame.body);
    }

    this.stompClient?.activate();
  }

  sendMessage(msg: string) {
    if(this.stompClient && this.stompClient.connected) {
      this.stompClient?.publish({
        destination: '/app/tbd',
        body: JSON.stringify({ message: msg })
      });
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}
