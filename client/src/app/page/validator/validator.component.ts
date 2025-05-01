import { Component, inject, OnInit, signal } from '@angular/core';
import { WalletComponent } from '../../component/wallet/wallet.component';
import { ValidatorService } from '../../service/validator.service';
import { ValidatorDto } from '../../model/validator.type';
import { HubService } from '../../service/hub.service';

@Component({
  selector: 'app-validator',
  imports: [WalletComponent],
  templateUrl: './validator.component.html',
  styleUrl: './validator.component.css'
})
export class ValidatorComponent implements OnInit {
  validatorService = inject(ValidatorService);
  hubService = inject(HubService);

  wallet = signal(false);
  validator = signal<ValidatorDto>({
    email: '',
    accounId: '',
    pubKey: '',
    balance: 0,
    payout: 0
  })

  ngOnInit(): void {
    this.validatorService.getValidator().subscribe({
      next: (res) => {
        this.validator.set(res);
      }
    });

    this.hubService.connected.subscribe({
      next: () => {
        console.log("connected");
                
      }
    })
  }

  connect() {
    this.hubService.connect();
  }

  toggleWallet() {
    this.wallet.update(toggle => !toggle);
  }
}
