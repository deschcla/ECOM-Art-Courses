import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AcheteurService } from '../service/acheteur.service';

import { AcheteurComponent } from './acheteur.component';

describe('Acheteur Management Component', () => {
  let comp: AcheteurComponent;
  let fixture: ComponentFixture<AcheteurComponent>;
  let service: AcheteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'acheteur', component: AcheteurComponent }]), HttpClientTestingModule],
      declarations: [AcheteurComponent],
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
      .overrideTemplate(AcheteurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcheteurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AcheteurService);

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
    expect(comp.acheteurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to acheteurService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAcheteurIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAcheteurIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
