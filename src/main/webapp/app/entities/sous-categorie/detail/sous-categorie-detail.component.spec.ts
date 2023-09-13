import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SousCategorieDetailComponent } from './sous-categorie-detail.component';

describe('SousCategorie Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SousCategorieDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SousCategorieDetailComponent,
              resolve: { sousCategorie: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(SousCategorieDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load sousCategorie on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SousCategorieDetailComponent);

      // THEN
      expect(instance.sousCategorie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
