import {Produit} from "./course.model";

export class LigneCommande {
    constructor(
        public idLigneCommande: number,
        public produit: Produit,
        public quantite: number,
    ) {}
}
