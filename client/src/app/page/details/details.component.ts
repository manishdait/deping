import { Component, inject, OnInit, signal } from '@angular/core';
import { WebsiteService } from '../../service/website.service';
import { ActivatedRoute } from '@angular/router';
import { WebSiteDto } from '../../model/website.type';

@Component({
  selector: 'app-details',
  imports: [],
  templateUrl: './details.component.html',
  styleUrl: './details.component.css'
})
export class DetailsComponent implements OnInit {
  route = inject(ActivatedRoute);
  websiteService = inject(WebsiteService);

  id = signal(this.route.snapshot.params['id']);
  details = signal<WebSiteDto>({
    id: 0,
    url: '',
    ticks: []
  });

  ngOnInit(): void {
    this.websiteService.fetchUserWebsite(this.id()).subscribe({
      next: (res) => {
        this.details.set(res);
      }
    })
  }

  getStatus() {
    if (this.details().ticks[this.details().ticks.length-1]) {
      return this.details().ticks[this.details().ticks.length-1].status;
    }
    return 'UNKNOWN';
  }

  getTime(timestamp: Date) {
    const date = new Date(timestamp);
    return date.getHours() + ':' + date.getMinutes();
  }
}
