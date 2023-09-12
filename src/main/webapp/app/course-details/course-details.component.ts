import { Component, OnInit } from '@angular/core';
import { Course } from '../core/request/course.model';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'jhi-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss'],
})
export class CourseDetailsComponent implements OnInit {
  idProduit: number;
  course: Course;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.idProduit = params['id'];
      this.course = {
        idProduit: this.idProduit,
        nomProduit: 'Cours de Piano',
        desc: 'Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.',
        tarifUnit: 50,
        quantiteDispo: 10,
        lienImg:
          'https://images.unsplash.com/photo-1585038021831-8afd9f9ab27f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1470&q=80',
        idSousCategorie: 1,
        dateTime: new Date('2023-09-12T09:00:00'),
        duree: 3,
        promo: 50,
        quantiteTotale: 50,
        nomProf: 'Jeanne Marie',
      };
    });
  }

  public counter(i: number): any[] {
    return new Array(i);
  }
}
