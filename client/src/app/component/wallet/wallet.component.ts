import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { WalletService } from '../../service/wallet.service';
import { TransferHbarRequest, WalletDto } from '../../model/wallet.type';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-wallet',
  imports: [FontAwesomeModule, ReactiveFormsModule],
  templateUrl: './wallet.component.html',
  styleUrl: './wallet.component.css'
})
export class WalletComponent implements OnInit {
  cancel = output<boolean>();

  walletService = inject(WalletService);
  faLibrary = inject(FaIconLibrary);
  
  wallet = signal<WalletDto>({
    uname: '',
    accountId: '',
    pubKey: '',
    balance: 0
  });

  loading = signal(true);

  form: FormGroup;

  constructor() {
    this.form = new FormGroup({
      pubKey: new FormControl(''),
      amount: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);

    this.walletService.getWallet().subscribe({
      next: (res) => {
        this.wallet.set(res);
        this.loading.set(false);
      }
    });
  }

  transfer() {
    const req: TransferHbarRequest = {
      pubKey: this.form.get('pubKey')?.value,
      amount: this.form.get('amount')?.value
    }

    this.walletService.transferHbar(req).subscribe({
      next: (res) => {
        this.wallet.set(res);
      }
    })
  }
  
  copyAddress() {
    navigator.clipboard.writeText(this.wallet().pubKey).then(() => {
      alert('Copied')
    })
  }

  toggleCancel() {
    this.cancel.emit(true);
  }
}
