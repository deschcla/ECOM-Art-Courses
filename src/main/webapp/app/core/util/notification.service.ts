import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { NotificationType, INotification } from '../../entities/notification/notification.model';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor() {}

  private notification$ = new BehaviorSubject<INotification | null>(null);

  success(message: string, duration: number): void {
    this.notify(message, NotificationType.Success, duration);
  }
  warning(message: string, duration: number): void {
    this.notify(message, NotificationType.Warning, duration);
  }
  error(message: string, duration: number): void {
    this.notify(message, NotificationType.Error, duration);
  }
  notify(message: string, type: NotificationType, duration: number): void {
    duration = !duration ? 3000 : duration;
    this.notification$.next({
      message: message,
      type: type,
      duration: duration,
    } as INotification);
  }
  get notification() {
    return this.notification$.asObservable();
  }

  notifyBanner(type, message): void {
    switch (type) {
      case 'Success':
        setTimeout(() => {
          this.success(message, 1500); // duration 1.5s notification disappear
        }, 200); // wait 0.2s show a notification
        break;
      case 'Warning':
        setTimeout(() => {
          this.warning(message, 1500);
        }, 200);
        break;
      case 'Error':
        setTimeout(() => {
          this.error(message, 1500);
        }, 200);
        break;
    }
  }
}
