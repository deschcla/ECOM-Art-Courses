import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Course } from 'app/core/request/course.model';
import { Subject } from 'rxjs';

@Component({
  selector: 'jhi-course-search',
  templateUrl: './course-search.component.html',
  styleUrls: ['./course-search.component.scss'],
})
export class CourseSearchComponent implements OnInit, OnDestroy {
  courses: Course[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.courses.push(
      {
        idProduit: 1,
        nomProduit: 'Cours de Piano',
        desc: 'Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.',
        tarifUnit: 50,
        quantiteDispo: 3,
        lienImg: '/content/images/course images/piano.jpg',
        idSousCategorie: 1,
        dateTime: new Date('2023-09-12T09:00:00'),
        duree: 3,
        promo: null,
        quantiteTotale: 50,
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
        dateTime: new Date('2023-10-12T15:00:00'),
        duree: 2,
        promo: 20,
        quantiteTotale: 30,
        idProf: 2,
      },
      {
        idProduit: 1,
        nomProduit: 'Cours de Piano',
        desc: 'Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.',
        tarifUnit: 50,
        quantiteDispo: 3,
        lienImg: '/content/images/course images/piano.jpg',
        idSousCategorie: 1,
        dateTime: new Date('2023-09-12T09:00:00'),
        duree: 3,
        promo: null,
        quantiteTotale: 50,
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
        dateTime: new Date('2023-10-12T15:00:00'),
        duree: 2,
        promo: 20,
        quantiteTotale: 30,
        idProf: 2,
      },
      {
        idProduit: 1,
        nomProduit: 'Cours de Piano',
        desc: 'Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.',
        tarifUnit: 50,
        quantiteDispo: 3,
        lienImg: '/content/images/course images/piano.jpg',
        idSousCategorie: 1,
        dateTime: new Date('2023-09-12T09:00:00'),
        duree: 3,
        promo: null,
        quantiteTotale: 50,
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
        dateTime: new Date('2023-10-12T15:00:00'),
        duree: 2,
        promo: 20,
        quantiteTotale: 30,
        idProf: 2,
      },
      {
        idProduit: 2,
        nomProduit: 'Cours de Piano Pour Enfants',
        desc: 'Notre cours est conçu spécialement pour les petits musiciens en pleine croissance, offrant une introduction amusante et éducative au piano. Avec une atmosphère chaleureuse, les enfants apprendront les compétences de base du piano tout en explorant leur créativité musicale. Ce cours est une opportunité de développer la motricité fine, la discipline et l’appréciation de la musique.',
        tarifUnit: 50,
        quantiteDispo: 22,
        lienImg: '/content/images/course images/piano-enfants.jpg',
        idSousCategorie: 1,
        dateTime: new Date('2023-10-12T15:00:00'),
        duree: 2,
        promo: 20,
        quantiteTotale: 30,
        idProf: 2,
      }
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  viewDetails(): void {
    this.router.navigate(['/course-details']);
  }
}
