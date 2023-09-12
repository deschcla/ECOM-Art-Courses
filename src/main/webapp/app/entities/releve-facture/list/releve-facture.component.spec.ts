import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReleveFactureService } from '../service/releve-facture.service';

import { ReleveFactureComponent } from './releve-facture.component';

describe('ReleveFacture Management Component', () => {
  let comp: ReleveFactureComponent;
  let fixture: ComponentFixture<ReleveFactureComponent>;
  let service: ReleveFactureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'releve-facture', component: ReleveFactureComponent }]),
        HttpClientTestingModule,
        ReleveFactureComponent,
      ],
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
      .overrideTemplate(ReleveFactureComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReleveFactureComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReleveFactureService);

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
    expect(comp.releveFactures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to releveFactureService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getReleveFactureIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getReleveFactureIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
