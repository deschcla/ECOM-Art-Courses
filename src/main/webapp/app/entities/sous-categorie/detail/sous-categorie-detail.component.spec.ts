import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SousCategorieDetailComponent } from './sous-categorie-detail.component';

describe('SousCategorie Management Detail Component', () => {
  let comp: SousCategorieDetailComponent;
  let fixture: ComponentFixture<SousCategorieDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SousCategorieDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sousCategorie: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SousCategorieDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SousCategorieDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sousCategorie on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sousCategorie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
