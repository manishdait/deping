import { Component, inject, OnInit, signal } from '@angular/core';
import { WalletComponent } from '../../component/wallet/wallet.component';
import { HubService } from '../../service/hub.service';
import { TicksDto } from '../../model/ticks.type';
import { PayoutService } from '../../service/payout.service';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { AuthService } from '../../service/auth.service';
import { UptimeService } from '../../service/uptime.service';
import { WebsiteResponse } from '../../model/website.type';

@Component({
  selector: 'app-validator',
  imports: [WalletComponent, FontAwesomeModule],
  templateUrl: './validator.component.html',
  styleUrl: './validator.component.css'
})
export class ValidatorComponent implements OnInit {
  authService = inject(AuthService);
  payoutService = inject(PayoutService);
  uptimeService = inject(UptimeService);
  hubService = inject(HubService);
  faLibrary = inject(FaIconLibrary);

  wallet = signal(false);
  startedValidating = signal(false);

  username = signal(this.authService.username);
  payouts = signal(0);

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);

    this.getPayouts();

    this.hubService.connected.subscribe({
      next: (res) => {
        this.startedValidating.set(res);
      }
    });

    this.hubService.validateRequest.subscribe({
      next: (res) => {
        this.checkUptime(res);
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

  checkUptime(website: WebsiteResponse) {
    this.uptimeService.getUptime(website.url)
      .then((res) => {
        const req: TicksDto = {
          status: 'UP',
          websiteId: website.id,
          validator: ''
        }
    
        this.hubService.sendMessage(req);
        this.getPayouts();
      })
      .catch((err) => {
        const req: TicksDto = {
          status: 'DOWN',
          websiteId: website.id,
          validator: ''
        }
    
        this.hubService.sendMessage(req);
        this.getPayouts();
      }); 
  }

  startValidate() {
    this.hubService.connect();
  }

  stopValidate() {
    this.hubService.disconnect();
  }
}
