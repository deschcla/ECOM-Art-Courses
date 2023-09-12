import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CommandeDetailComponent } from './commande-detail.component';

describe('Commande Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommandeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CommandeDetailComponent,
              resolve: { commande: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CommandeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load commande on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CommandeDetailComponent);

      // THEN
      expect(instance.commande).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
