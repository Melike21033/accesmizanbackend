package com.mizanlabs.mr.entities;
import java.util.List;
public class UnitesResponse {
        private List<Unite> unites;
        private Unite unitePrincipale;

        // Constructeurs, getters et setters
        public UnitesResponse(List<Unite> unites, Unite unitePrincipale) {
            this.unites = unites;
            this.unitePrincipale = unitePrincipale;
        }

        public List<Unite> getUnites() {
            return unites;
        }

        public void setUnites(List<Unite> unites) {
            this.unites = unites;
        }

        public Unite getUnitePrincipale() {
            return unitePrincipale;
        }

        public void setUnitePrincipale(Unite unitePrincipale) {
            this.unitePrincipale = unitePrincipale;
        }
    }