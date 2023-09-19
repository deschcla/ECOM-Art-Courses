export enum Difficulty {
    beginner = "Beginner",
    intermediate = "Intermediate",
    advanced = "Advanced"
}

export class Produit {
    constructor(
        public idProduit: number,
        public nomProduit: string,
        public desc: string | null,
        public tarifUnit: number,
        public quantiteDispo: number,
        public lienImg: string,
        public idSousCategorie: number,
        public dateTime: Date,
        public horaire: string,
        public promo: number | null,
        public quantiteTotale: number,
        public idProf: number
    ) {}
}