import { Component, signal } from '@angular/core';
import { UrlFormComponent } from '../../component/url-form/url-form.component';

@Component({
  selector: 'app-user',
  imports: [UrlFormComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent {
  addUrl = signal(false);
}
