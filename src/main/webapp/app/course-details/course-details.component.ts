import { Component, OnInit } from '@angular/core';
import { Course } from '../core/request/course.model';
import { ActivatedRoute, Params } from '@angular/router';

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
      this.course = window.history.state;
    });
  }

  public counter(i: number): any[] {
    return new Array(i);
  }
}
