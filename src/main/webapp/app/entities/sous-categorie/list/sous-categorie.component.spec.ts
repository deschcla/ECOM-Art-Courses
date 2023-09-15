import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SousCategorieService } from '../service/sous-categorie.service';

import { SousCategorieComponent } from './sous-categorie.component';

describe('SousCategorie Management Component', () => {
  let comp: SousCategorieComponent;
  let fixture: ComponentFixture<SousCategorieComponent>;
  let service: SousCategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'sous-categorie', component: SousCategorieComponent }]), HttpClientTestingModule],
      declarations: [SousCategorieComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(SousCategorieComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousCategorieComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SousCategorieService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.sousCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to sousCategorieService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getSousCategorieIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getSousCategorieIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
