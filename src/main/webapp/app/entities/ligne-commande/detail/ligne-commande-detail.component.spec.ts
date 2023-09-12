import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LigneCommandeDetailComponent } from './ligne-commande-detail.component';

describe('LigneCommande Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LigneCommandeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LigneCommandeDetailComponent,
              resolve: { ligneCommande: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(LigneCommandeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ligneCommande on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LigneCommandeDetailComponent);

      // THEN
      expect(instance.ligneCommande).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
