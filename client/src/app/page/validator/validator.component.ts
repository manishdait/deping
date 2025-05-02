import { Component, inject, OnInit, signal } from '@angular/core';
import { WalletComponent } from '../../component/wallet/wallet.component';
import { HubService } from '../../service/hub.service';
import { TicksRequest } from '../../model/ticks.type';
import { PayoutService } from '../../service/payout.service';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-validator',
  imports: [WalletComponent, FontAwesomeModule],
  templateUrl: './validator.component.html',
  styleUrl: './validator.component.css'
})
export class ValidatorComponent implements OnInit {
  authService = inject(AuthService);
  payoutService = inject(PayoutService);
  hubService = inject(HubService);
  faLibrary = inject(FaIconLibrary);

  wallet = signal(false);

  username = signal(this.authService.username);
  payouts = signal(0);

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);

    this.getPayouts();

    this.hubService.connected.subscribe({
      next: () => {
        console.log("connected");  
      }
    });

    this.hubService.messageRecive.subscribe({
      next: (res) => {
        this.tbd(res.url);
      }
    })
  }

  toggleWallet() {
    this.wallet.update(toggle => !toggle);
  }

  getPayouts() {
    this.payoutService.getPayouts().subscribe({
      next: (res) => {
        this.payouts.set(res.payout);
      }
    })
  }

  claimPayouts() {
    this.payoutService.claimPayouts().subscribe({
      next: (res) => {
        console.log(res);
      }
    });
  }

  tbd(msg: string) {
    const req: TicksRequest = {
      status: 'Clinet ' + msg,
      websiteId: 0,
      validator: ''
    }

    this.hubService.sendMessage(req);
    this.getPayouts();
  }

  connect() {
    this.hubService.connect();
  }
}
