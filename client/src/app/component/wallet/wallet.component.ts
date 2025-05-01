import { Component, inject, input, OnInit, output } from '@angular/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { ValidatorDto } from '../../model/validator.type';

@Component({
  selector: 'app-wallet',
  imports: [FontAwesomeModule],
  templateUrl: './wallet.component.html',
  styleUrl: './wallet.component.css'
})
export class WalletComponent implements OnInit {
  validator = input.required<ValidatorDto>();
  cancel = output<boolean>();

  faLibrary = inject(FaIconLibrary);

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons);
  }

  toggleCancel() {
    this.cancel.emit(true);
  }
}
