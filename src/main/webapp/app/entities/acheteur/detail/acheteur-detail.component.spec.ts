import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AcheteurDetailComponent } from './acheteur-detail.component';

describe('Acheteur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcheteurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AcheteurDetailComponent,
              resolve: { acheteur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(AcheteurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load acheteur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AcheteurDetailComponent);

      // THEN
      expect(instance.acheteur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
