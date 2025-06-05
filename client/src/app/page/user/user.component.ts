import { Component, inject, OnInit, signal } from '@angular/core';
import { UrlFormComponent } from '../../component/url-form/url-form.component';
import { WebsiteService } from '../../service/website.service';
import { WebsiteResponse } from '../../model/website.type';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fontawsomeIcons } from '../../shared/fa-icons';
import { TicksResponse } from '../../model/ticks.type';

@Component({
  selector: 'app-user',
  imports: [FontAwesomeModule, UrlFormComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {
  websiteService = inject(WebsiteService);
  faLibrary = inject(FaIconLibrary);

  addUrl = signal(false);
  websites = signal<WebsiteResponse[]>([]);
  ticks = signal<{[key:number]: {open: boolean, ticks: TicksResponse[]}}>({});

  toggleAddUrl() {
    this.addUrl.update(toggle => !toggle);
  }

  ngOnInit(): void {
    this.faLibrary.addIcons(...fontawsomeIcons)
    this.websiteService.fetchUserWebsites().subscribe({
      next: (res) => {
        this.websites.set(res.content);
        for (let url of res.content) {
          this.ticks()[url.id] = {open: false, ticks: []};
        }
      }
    })
  }

  getTicks(id: number) {
    if (this.ticks()[id] && this.ticks()[id].open) {
      this.ticks()[id].open = false;
      return;
    }

    this.websiteService.fetchWebsitesTicks(id).subscribe({
      next: (res) => {
        this.ticks()[id].ticks = res;
        this.ticks()[id].open = true;
      }
    })
  }
}
