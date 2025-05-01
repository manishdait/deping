import { Component, inject, output, signal} from '@angular/core';
import { WebsiteService } from '../../service/website.service';
import { WebsiteRequest } from '../../model/website.type';

@Component({
  selector: 'app-url-form',
  imports: [],
  templateUrl: './url-form.component.html',
  styleUrl: './url-form.component.css'
})
export class UrlFormComponent {
  cancel = output<boolean>();

  websiteService = inject(WebsiteService);

  errors = signal(false);

  toggleCancel() {
    this.cancel.emit(true);
  }

  create(url: string) {
    if (url.length == 0) {
      this.errors.set(true);
      return;
    }

    this.errors.set(false);

    const req: WebsiteRequest = {
      url: url
    }

    this.websiteService.createWebsite(req).subscribe({
      next: (res) => {
        console.log(res);
      }
    })
  }
}
