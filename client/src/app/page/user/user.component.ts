import { Component, inject, OnInit, signal } from '@angular/core';
import { UrlFormComponent } from '../../component/url-form/url-form.component';
import { WebsiteService } from '../../service/website.service';
import { WebsiteResponse } from '../../model/website.type';

@Component({
  selector: 'app-user',
  imports: [UrlFormComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {
  websiteService = inject(WebsiteService);

  addUrl = signal(false);
  websites = signal<WebsiteResponse[]>([]);

  toggleAddUrl() {
    this.addUrl.update(toggle => !toggle);
  }

  ngOnInit(): void {
    this.websiteService.fetchUserWebsites().subscribe({
      next: (res) => {
        this.websites.set(res.content);
      }
    })
  }
}
