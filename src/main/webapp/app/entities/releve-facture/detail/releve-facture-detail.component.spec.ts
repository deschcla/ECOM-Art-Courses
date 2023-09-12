import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReleveFactureDetailComponent } from './releve-facture-detail.component';

describe('ReleveFacture Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReleveFactureDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReleveFactureDetailComponent,
              resolve: { releveFacture: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ReleveFactureDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load releveFacture on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReleveFactureDetailComponent);

      // THEN
      expect(instance.releveFacture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
