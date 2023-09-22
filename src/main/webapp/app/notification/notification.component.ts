import { Component, ElementRef, OnInit, Renderer2, ViewChild } from '@angular/core';
import { NotificationService } from '../core/util/notification.service';
import { INotification, NotificationType } from '../entities/notification/notification.model';
import { takeWhile } from 'rxjs';

@Component({
  selector: 'jhi-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
})
export class NotificationComponent implements OnInit {
  @ViewChild('notificationContainer') container: ElementRef<HTMLDivElement>;
  constructor(private service: NotificationService, private renderer: Renderer2) {}

  private _subscribed: boolean = true;
  private classMap: Map<NotificationType, string>;
  ngOnInit(): void {
    this.classMap = new Map<NotificationType, string>();
    this.classMap.set(NotificationType.Success, 'success');
    this.classMap.set(NotificationType.Warning, 'warning');
    this.classMap.set(NotificationType.Error, 'error');

    this.service.notification.pipe(takeWhile(() => this._subscribed)).subscribe(notification => {
      if (notification) {
        this.render(notification);
      }
    });
  }
  ngOnDestroy(): void {
    this._subscribed = false;
  }
  private render(notification: INotification): void {
    let notificationBox = this.renderer.createElement('div');
    let content = this.renderer.createElement('div');

    const boxColorClass = this.classMap.get(notification.type);
    let classesToAdd = ['message-box', boxColorClass];
    classesToAdd.forEach(x => this.renderer.addClass(notificationBox, x!));

    this.renderer.setStyle(notificationBox, 'transition', `opacity ${notification.duration}ms`);
    this.renderer.setStyle(notificationBox, 'opacity', '1');

    const text = this.renderer.createText(notification.message);
    this.renderer.appendChild(content, text);

    this.renderer.appendChild(document.querySelector('.notif'), notificationBox);
    this.renderer.appendChild(notificationBox, content);

    setTimeout(() => {
      this.renderer.setStyle(notificationBox, 'opacity', '0');
      setTimeout(() => {
        this.renderer.removeChild(this.container.nativeElement, notificationBox);
      }, notification.duration);
    }, notification.duration);
  }
}
