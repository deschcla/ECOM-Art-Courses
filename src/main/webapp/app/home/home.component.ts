import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { faCartPlus } from '@fortawesome/free-solid-svg-icons';
import { Course, Difficulty } from 'app/core/request/course.model';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [SharedModule, RouterModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  faCartPlus = faCartPlus;
  account: Account | null = null;

  courses: Course[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.courses.push(
      {
        idProduit: 1,
        nomProduit: 'Cours de Piano',
        desc: 'Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.',
        tarifUnit: 50,
        quantiteDispo: 3,
        lienImg: '/content/images/course images/piano.jpg',
        idSousCategorie: 1,
        dateDebut: new Date('12-09-2023'),
        dateFin: new Date('12-10-2023'),
        horaire: 'mondays from 4pm till 6pmmmmmmmmmmmmmmmmmmmm',
        promo: null,
        quantiteTotale: 50,
        difficulte: Difficulty.intermediate,
        idProf: 1,
      },
      {
        idProduit: 2,
        nomProduit: 'Cours de Piano Pour Enfants',
        desc: 'Notre cours est conçu spécialement pour les petits musiciens en pleine croissance, offrant une introduction amusante et éducative au piano. Avec une atmosphère chaleureuse, les enfants apprendront les compétences de base du piano tout en explorant leur créativité musicale. Ce cours est une opportunité de développer la motricité fine, la discipline et l’appréciation de la musique.',
        tarifUnit: 50,
        quantiteDispo: 22,
        lienImg: '/content/images/course images/piano-enfants.jpg',
        idSousCategorie: 1,
        dateDebut: new Date('15-09-2023'),
        dateFin: new Date('15-12-2023'),
        horaire: 'weekends from 2pm till 3pm',
        promo: 20,
        quantiteTotale: 30,
        difficulte: Difficulty.beginner,
        idProf: 2,
      }
    );
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewDetails(): void {
    this.router.navigate(['/course-details']);
  }
}
