import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AcheteurFormService } from './acheteur-form.service';
import { AcheteurService } from '../service/acheteur.service';
import { IAcheteur } from '../acheteur.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { AcheteurUpdateComponent } from './acheteur-update.component';

describe('Acheteur Management Update Component', () => {
  let comp: AcheteurUpdateComponent;
  let fixture: ComponentFixture<AcheteurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acheteurFormService: AcheteurFormService;
  let acheteurService: AcheteurService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AcheteurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AcheteurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcheteurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acheteurFormService = TestBed.inject(AcheteurFormService);
    acheteurService = TestBed.inject(AcheteurService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const acheteur: IAcheteur = { id: 456 };
      const internalUser: IUser = { id: 13026 };
      acheteur.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 24548 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ acheteur });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const acheteur: IAcheteur = { id: 456 };
      const internalUser: IUser = { id: 28046 };
      acheteur.internalUser = internalUser;

      activatedRoute.data = of({ acheteur });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.acheteur).toEqual(acheteur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcheteur>>();
      const acheteur = { id: 123 };
      jest.spyOn(acheteurFormService, 'getAcheteur').mockReturnValue(acheteur);
      jest.spyOn(acheteurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acheteur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acheteur }));
      saveSubject.complete();

      // THEN
      expect(acheteurFormService.getAcheteur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(acheteurService.update).toHaveBeenCalledWith(expect.objectContaining(acheteur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcheteur>>();
      const acheteur = { id: 123 };
      jest.spyOn(acheteurFormService, 'getAcheteur').mockReturnValue({ id: null });
      jest.spyOn(acheteurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acheteur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acheteur }));
      saveSubject.complete();

      // THEN
      expect(acheteurFormService.getAcheteur).toHaveBeenCalled();
      expect(acheteurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAcheteur>>();
      const acheteur = { id: 123 };
      jest.spyOn(acheteurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acheteur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acheteurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
