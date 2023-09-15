import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReleveFactureDetailComponent } from './releve-facture-detail.component';

describe('ReleveFacture Management Detail Component', () => {
  let comp: ReleveFactureDetailComponent;
  let fixture: ComponentFixture<ReleveFactureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReleveFactureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ releveFacture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReleveFactureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReleveFactureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load releveFacture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.releveFacture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
