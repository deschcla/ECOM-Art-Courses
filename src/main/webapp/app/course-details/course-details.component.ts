import { Component } from '@angular/core';
import {Course} from "../core/request/course.model";

@Component({
  selector: 'jhi-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.scss']
})
export class CourseDetailsComponent {



  course: Course = {
    idProduit: 1,
    nomProduit: "Cours de Piano",
    desc: "Découvrez le monde de la musique à travers les touches du piano! Notre cours de piano offre un voyage passionnant pour les débutants et avancés. Vous développerez des compétences techniques, une expression artistique et un lien profond avec la musique. Rejoignez-nous et embarquez pour une aventure musicale qui enrichira votre vie et vous procurera une joie durable.",
    tarifUnit: 50,
    quantiteDispo: 3,
    lienImg: "/content/images/course images/piano.jpg",
    idSousCategorie: 1,
    dateTime: new Date('2023-09-12T22:00:00'),
    horaire: "mondays from 4pm till 6pm",
    promo: 20,
    quantiteTotale: 50,
    idProf: 1
  };

  public counter(i: number): any[] {
    return new Array(i);
  }


}
