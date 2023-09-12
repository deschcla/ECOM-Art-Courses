import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CarteBancaireDetailComponent } from './carte-bancaire-detail.component';

describe('CarteBancaire Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarteBancaireDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CarteBancaireDetailComponent,
              resolve: { carteBancaire: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CarteBancaireDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load carteBancaire on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CarteBancaireDetailComponent);

      // THEN
      expect(instance.carteBancaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
