/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright Â© 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.maidard;

import com.objetdirect.engine.EnumDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.engine.TypeDescriptor;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ManyToManyReferenceListDescriptor;
import com.objetdirect.entities.ManyToOneReferenceDescriptor;
import com.objetdirect.entities.OneToManyReferenceListDescriptor;
import com.objetdirect.entities.OneToOneReferenceDescriptor;
import com.objetdirect.entities.RelationshipDescriptor;
import com.objetdirect.entities.UnicityDescriptor;
import com.objetdirect.entities.ValueObjectDescriptor;

public class GenerateAgora {

	void generateEntities() {

		boolean secured = true;
		
		// Habilitations et autorisations
		EnumDescriptor sexEnum = new EnumDescriptor("com.maidard.agora.domain", "SexEnum")
			.addConstant("MALE", "male") 
			.addConstant("FEMALE", "female");

		EnumDescriptor tatooTypeEnum = new EnumDescriptor("com.maidard.agora.domain", "TatooTypeEnum")
			.addConstant("NONE", "none") 
			.addConstant("TATTOO", "tattoo")
			.addConstant("CHIP", "chip");

		EnumDescriptor tatooLocationEnum = new EnumDescriptor("com.maidard.agora.domain", "TatooLocationEnum")
			.addConstant("SHOULDER", "shoulder") 
			.addConstant("NECK", "neck")
			.addConstant("OTHER", "other");

		EntityDescriptor animal = new EntityDescriptor("com.maidard.agora.domain", "Animal", secured)
			.addStringField("name", null)
			.addDateField("dateOfBirth", null)
			.addStringField("deptOfBirth", null)
			.addStringField("cityOfBirth", null)
			.addStringField("race", null)
			.addStringField("origin", null)
			.addEnumField("sex", sexEnum, null)
			.addStringField("color", null)
			.addStringField("otherDetails", null)
			.addStringField("comment", null)
			.addEnumField("tattoo", tatooTypeEnum, null)
			.addEnumField("tattooLocation", tatooLocationEnum, null)
			.addBooleanField("sterilized", null)
			.addStringField("behavior", null)
			.addStringField("characteristics", null)
			.addBooleanField("communicationAuthorization", null);

		EntityDescriptor animalCategory = new EntityDescriptor("com.maidard.agora.domain", "AnimalCategory", secured)
			.addStringField("name", null)
			.addStringField("descriptop,", null)
			.addStringField("image", null);
		
		EntityDescriptor animalType = new EntityDescriptor("com.maidard.agora.domain", "AnimalType", secured)
			.addStringField("name", null)
			.addStringField("descriptop,", null)
			.addStringField("image", null);

		animalType.addManyToOne(animalCategory, "category", false, false, "types", false);
		animal.addManyToOne(animalType, "type", false, false);

		EnumDescriptor genderEnum = new EnumDescriptor("com.maidard.agora.domain", "GenderEnum")
			.addConstant("MISTER", "Mr.") 
			.addConstant("MISS", "Ms.")
			.addConstant("MADAM", "Mrs.");

		EntityDescriptor animalOwner = new EntityDescriptor("com.maidard.agora.domain", "AnimalOwner", secured)
			.addEnumField("gender", genderEnum, null)
			.addStringField("firstName", null)
			.addStringField("lastName", null)
			.addStringField("adressStreet", null)
			.addStringField("adressZipCode", null)
			.addStringField("adressCity", null)
			.addStringField("personnalPhone", null)
			.addStringField("professionalPhone", null)
			.addStringField("mobile", null);

		EntityDescriptor animalOwnership = new EntityDescriptor("com.maidard.agora.domain", "AnimalOwnership", secured)
			.addStringField("startDate", null)
			.addStringField("endDate", null);

		animalOwnership.addManyToOne(animal, "animal", false, false, "owners", false);
		animalOwnership.addManyToOne(animalOwner, "owner", false, false, "animals", false);
		
		EnumDescriptor identityDocTypeEnum = new EnumDescriptor("com.maidard.agora.domain", "IdentityDocTypeEnum")
			.addConstant("MONE", "none") 
			.addConstant("EUROPEAN PASSPORT", "European Passport");

		EntityDescriptor identityDocument = new EntityDescriptor("com.maidard.agora.domain", "IdentityDocument", secured)
			.addEnumField("identityDocType", identityDocTypeEnum, null)
			.addStringField("number", null)
			.addDateField("dateOfIssue", null);

		identityDocument.addManyToOne(animal, "target", false, false, "identities", false);
		
		EntityDescriptor animalMeasure = new EntityDescriptor("com.maidard.agora.domain", "AnimalMeasure", secured)
			.addDateField("dateOfMeasure", null)
			.addDoubleField("size", null)
			.addDoubleField("weight", null);
		
		animalMeasure.addManyToOne(animal, "target", false, false, "measures", false);
		
		EntityDescriptor veterinarySurgeon = new EntityDescriptor("com.maidard.agora.domain", "VeterinarySurgeon", secured)
			.addEnumField("gender", genderEnum, null)
			.addStringField("firstName", null)
			.addStringField("lastName", null)
			.addStringField("adressStreet", null)
			.addStringField("adressZipCode", null)
			.addStringField("adressCity", null)
			.addStringField("professionalPhone", null)
			.addStringField("mobile", null);
		
		EntityDescriptor treat = new EntityDescriptor("com.maidard.agora.domain", "Treat", secured)
			.addStringField("startDate", null)
			.addStringField("endDate", null);

		treat.addManyToOne(animal, "animal", false, false, "surgeons", false);
		treat.addManyToOne(veterinarySurgeon, "surgeon", false, false, "animals", false);

		EntityDescriptor consultation = new EntityDescriptor("com.maidard.agora.domain", "Consultation", secured)
			.addStringField("number", null)
			.addDateField("date", null)
			.addStringField("firstName", null);
		
		
		
		
		
		EntityDescriptor role = new EntityDescriptor("com.ockham.agora.domain", "Role", secured).
			addStringField("nom", null);
		
		EnumDescriptor rankType = new EnumDescriptor("com.ockham.agora.domain", "TypeDeGrade")
			.addConstant("GARDIEN_DE_LA_PAIX", "gardien de la paix") 
			.addConstant("SOUS_BRIGADIER", "sous-brigadier")
			.addConstant("BRIGADIER", "brigadier")
			.addConstant("BRIGADIER_CHEF", "brigadier-chef") 
			.addConstant("BRIGADIER_MAJOR", "brigardier-major")
			.addConstant("LIEUTENANT", "lieutenant")
			.addConstant("CAPITAINE", "capitaine")
			.addConstant("COMMANDANT", "commandant")
			.addConstant("COMMISSAIRE", "commissaire") 
			.addConstant("COMMISSAIRE_PRINCIPAL", "commissaire principal") 
			.addConstant("COMMISSAIRE_DIVISIONNAIRE", "commissaire divisionnaire");		
		
		EntityDescriptor user = new EntityDescriptor("com.ockham.agora.domain", "Utilisateur", secured).
			addStringField("login", null).
			addStringField("motDePasse", null).
			addStringField("nom", null).
			addStringField("prenom", null).
			addBooleanField("actif", null).
			addEnumField("grade", rankType, null).
			addStringField("matricule", null);
		
		user.addConstraint(new UnicityDescriptor("A user with this login already exist", user.getMember("login")));
		
		user.addRelationship(new ManyToManyReferenceListDescriptor(user, role, "roles", false));
		
		EntityDescriptor group = new EntityDescriptor("com.ockham.agora.domain", "Groupe", secured).
			addStringField("nom", null);
		
		RelationshipDescriptor groupToUser = new ManyToManyReferenceListDescriptor(group, user, "utilisateurs", false);
		RelationshipDescriptor userToGroup = new ManyToManyReferenceListDescriptor(user, group, "groupes", false);
		userToGroup.setReverse(groupToUser, true);
		groupToUser.setReverse(userToGroup, false);
		user.addRelationship(userToGroup);
		group.addRelationship(groupToUser);

		EntityDescriptor address = new EntityDescriptor("com.ockham.agora.domain", "Adresse", secured).
			addStringField("numero", null).
			addStringField("numeroBatiment", null).
			addStringField("typeDeVoie", null).
			addStringField("nomDeVoie", null).
			addStringField("localite", null).
			addStringField("pays", null);
	
		EntityDescriptor service = new EntityDescriptor("com.ockham.agora.domain", "Service", secured).
			addStringField("nom", null).
			addStringField("directionDeRattachement", null).
			addStringField("codeInsee", null).
			addStringField("nomDuChefDeService", null).
			addBooleanField("modeleDePersonnalisation", null);

		service.addRelationship(new OneToOneReferenceDescriptor(service, address, "adresse", true, true, true));

		RelationshipDescriptor serviceToGroup = new OneToManyReferenceListDescriptor(service, group, "groupes", false);
		RelationshipDescriptor groupToService = new ManyToOneReferenceDescriptor(group, service, "service", true, false);
		groupToService.setReverse(serviceToGroup, true);
		serviceToGroup.setReverse(groupToService, false);
		group.addRelationship(groupToService);
		service.addRelationship(serviceToGroup);
		
		RelationshipDescriptor serviceToUser = new OneToManyReferenceListDescriptor(service, user, "utilisateurs", false);
		RelationshipDescriptor userToService = new ManyToOneReferenceDescriptor(user, service, "service", false, false);
		userToService.setReverse(serviceToUser, true);
		serviceToUser.setReverse(userToService, false);
		user.addRelationship(userToService);
		service.addRelationship(serviceToUser);

		// Procédure et articulation
		EnumDescriptor legal = new EnumDescriptor("com.ockham.agora.domain", "CadreJuridique")
			.addConstant("FLAGRANT_DELIT", "flagrant délit")
			.addConstant("ENQUETE_PRELIMINAIRE", "enquête préliminaire")
			.addConstant("COMISSION_ROGATOIRE", "commission rogatoire")
			.addConstant("DECOUVERTE_DE_CADAVRE", "découverte de cadavre");

		EnumDescriptor legalRegime = new EnumDescriptor("com.ockham.agora.domain", "RegimeJuridique")
			.addConstant("DROIT_COMMUN", "droit commun")
			.addConstant("STUPEFIANTS", "stupéfiants")
			.addConstant("TERRORISME", "terrorisme")
			.addConstant("CRIMINALITE_ORGANISEE", "criminalité organisée");

		EnumDescriptor sortType = new EnumDescriptor("com.ockham.agora.domain", "OrdreArticulation")
			.addConstant("CHRONOLOGIQUE", "chronologique")
			.addConstant("LIBRE", "libre");

		EntityDescriptor procedure = new EntityDescriptor("com.ockham.agora.domain", "Procedure", secured).
			addStringField("numero", null).
			addEnumField("cadreJuridique", legal, null).
			addEnumField("regimeJuridique", legalRegime, null).
			addEnumField("ordreDArticulation", sortType, null).
			addBooleanField("transmis", "false");
		
		procedure.addRelationship(new ManyToOneReferenceDescriptor(procedure, group, "proprietaire", true, false));
		procedure.addRelationship(new ManyToOneReferenceDescriptor(procedure, user, "createur", true, false));

		EntityDescriptor folder = new EntityDescriptor("com.ockham.agora.domain", "Dossier", secured).
			addStringField("nom", null);

		RelationshipDescriptor procedureToFolder = new OneToManyReferenceListDescriptor(procedure, folder, "dossiers", true);
		RelationshipDescriptor folderToProcedure = new ManyToOneReferenceDescriptor(folder, procedure, "procedure", true, false);
		procedureToFolder.setReverse(folderToProcedure, false);
		folderToProcedure.setReverse(procedureToFolder, true);
		procedure.addRelationship(procedureToFolder);
		folder.addRelationship(folderToProcedure);

		EntityDescriptor custody = new EntityDescriptor("com.ockham.agora.domain", "GardeAVue", secured).
			addDateField("dateDebut", "new Date()").
			addDateField("dateFin", "null");
		
		RelationshipDescriptor procedureToCustory = new OneToManyReferenceListDescriptor(procedure, custody, "gardesAVue", true);
		RelationshipDescriptor custodyToProcedure = new ManyToOneReferenceDescriptor(custody, procedure, "procedure", true, false);
		procedureToCustory.setReverse(custodyToProcedure, false);
		custodyToProcedure.setReverse(procedureToCustory, true);
		procedure.addRelationship(procedureToCustory);
		custody.addRelationship(custodyToProcedure);

		EntityDescriptor offenseType = new EntityDescriptor("com.ockham.agora.domain", "TypeDeDelit", secured).
			addStringField("nom", null).
			addStringField("cadresJuridiquesApplicables", null); // il faudrait en fait un tableau d'énums ...

		RelationshipDescriptor procedureToOffenseType = new ManyToManyReferenceListDescriptor(procedure, offenseType, "typesDeDelits", false);
		RelationshipDescriptor offenseTypeToProcedure = new ManyToManyReferenceListDescriptor(offenseType, procedure, "procedures", false);
		procedureToOffenseType.setReverse(offenseTypeToProcedure, false);
		offenseTypeToProcedure.setReverse(procedureToOffenseType, true);
		procedure.addRelationship(procedureToOffenseType);
		offenseType.addRelationship(offenseTypeToProcedure);

		procedure.addRelationship(new ManyToOneReferenceDescriptor(procedure, offenseType, "typeDeDelitPrincipal", true, false));

		EntityDescriptor offenseFamily = new EntityDescriptor("com.ockham.agora.domain", "FamilleDeDelits", secured).
			addStringField("nom", null);
		
		RelationshipDescriptor offenseFamilyToOffenseType = new OneToManyReferenceListDescriptor(offenseFamily, offenseType, "typesDeDelit", false);
		RelationshipDescriptor offenseTypeToOffenseFamily = new ManyToOneReferenceDescriptor(offenseType, offenseFamily, "familleDeDelits", true, false);
		offenseTypeToOffenseFamily.setReverse(offenseFamilyToOffenseType, true);
		offenseFamilyToOffenseType.setReverse(offenseTypeToOffenseFamily, false);
		offenseType.addRelationship(offenseTypeToOffenseFamily);
		offenseFamily.addRelationship(offenseFamilyToOffenseType);

		EnumDescriptor editingStatus = new EnumDescriptor("com.ockham.agora.domain", "StatusDeRedaction")
			.addConstant("EN_COURS", "en cours")
			.addConstant("REDIGE", "rédigé");
		
		EntityDescriptor act = new EntityDescriptor("com.ockham.agora.domain", "Acte", secured).
			addEnumField("cadreJuridique", legal, null).
			addEnumField("regimeJuridique", legalRegime, null).
			addDateField("dateDeDebut", "new Date()").
			addDateField("dateDeFin", "null").
			addIntField("positionArticulation", null).
			addBooleanField("signe", "false").
			addDateField("dateDeSignature", "null").
			addBooleanField("detache", "false").
			addEnumField("statutDeRedaction", editingStatus, null).
			addStringField("document", null);

		act.addRelationship(new ManyToOneReferenceDescriptor(act, group, "proprietaire", true, false));
		act.addRelationship(new ManyToOneReferenceDescriptor(act, user, "createur", true, false));
	
		RelationshipDescriptor folderToAct = new OneToManyReferenceListDescriptor(folder, act, "actes", false);
		RelationshipDescriptor actToFolder = new ManyToOneReferenceDescriptor(act, folder, "dossier", false, false);
		actToFolder.setReverse(folderToAct, true);
		folderToAct.setReverse(actToFolder, false);
		act.addRelationship(actToFolder);
		folder.addRelationship(folderToAct);
		
		RelationshipDescriptor procedureToAct = new OneToManyReferenceListDescriptor(procedure, act, "actes", false);
		RelationshipDescriptor actToProcedure = new ManyToOneReferenceDescriptor(act, procedure, "procedure", false, false);
		actToProcedure.setReverse(procedureToAct, true);
		procedureToAct.setReverse(actToProcedure, false);
		act.addRelationship(actToProcedure);
		procedure.addRelationship(procedureToAct);

		RelationshipDescriptor custodyToAct = new OneToManyReferenceListDescriptor(custody, act, "actes", false);
		RelationshipDescriptor actToCustody = new ManyToOneReferenceDescriptor(act, custody, "gardeAVue", false, false);
		actToCustody.setReverse(custodyToAct, true);
		custodyToAct.setReverse(actToCustody, false);
		act.addRelationship(actToCustody);
		custody.addRelationship(custodyToAct);

		EnumDescriptor actCreationMode = new EnumDescriptor("com.ockham.agora.domain", "ModeDeCreationDActe")
			.addConstant("MODEL", "model")
			.addConstant("ASSISTED", "assisted");

		EnumDescriptor visibility = new EnumDescriptor("com.ockham.agora.domain", "VisibiliteDActe")
			.addConstant("PUBLIQUE", "publique")
			.addConstant("PRIVEE", "privée");

		EntityDescriptor actModel = new EntityDescriptor("com.ockham.agora.domain", "ModeleDActe", secured).
			addStringField("nom", null).
			addEnumField("modeDeCreation", actCreationMode, null).
			addStringField("document", null).
			addBooleanField("personnalise", null).
			addEnumField("visibilite", visibility, null).
			addBooleanField("aSigner", null);
		
		act.addRelationship(new ManyToOneReferenceDescriptor(act, actModel, "modele", false, false));

		EntityDescriptor actType = new EntityDescriptor("com.ockham.agora.domain", "TypeDActe", secured).
			addStringField("nom", null);

		EntityDescriptor actFamily = new EntityDescriptor("com.ockham.agora.domain", "FamilleDActes", secured).
			addStringField("nom", null);
		
		RelationshipDescriptor actFamilyToActType = new OneToManyReferenceListDescriptor(actFamily, actType, "types", false);
		RelationshipDescriptor actTypeToActFamily = new ManyToOneReferenceDescriptor(actType, actFamily, "famille", true, false);
		actTypeToActFamily.setReverse(actFamilyToActType, true);
		actFamilyToActType.setReverse(actTypeToActFamily, false);
		actModel.addRelationship(actTypeToActFamily);
		actFamily.addRelationship(actFamilyToActType);

		RelationshipDescriptor actTypeToActModel = new OneToManyReferenceListDescriptor(actType, actModel, "modeles", false);
		RelationshipDescriptor actModelToActType = new ManyToOneReferenceDescriptor(actModel, actType, "type", true, false);
		actModelToActType.setReverse(actTypeToActModel, true);
		actTypeToActModel.setReverse(actModelToActType, false);
		actModel.addRelationship(actModelToActType);
		actType.addRelationship(actTypeToActModel);

		EnumDescriptor alertSeverity = new EnumDescriptor("com.ockham.agora.domain", "GraviteDAlerte")
			.addConstant("FATALE", "fatale")
			.addConstant("SEVERE", "sévère")
			.addConstant("AVERTISSMENT", "avertissement")
			.addConstant("INFORMATION", "information")
			.addConstant("TRIVIALE", "triviale");

		EnumDescriptor alertVisibility = new EnumDescriptor("com.ockham.agora.domain", "VisibiliteDAlerte")
			.addConstant("PUBLIQUE", "publique")
			.addConstant("PRIVEE", "privée");

		EntityDescriptor alert = new EntityDescriptor("com.ockham.agora.domain", "Alerte", secured).
			addEnumField("visibilite", alertVisibility, null).
			addDateField("dateDebut", "new Date()").
			addDateField("dateFin", null).
			addEnumField("gravite", alertSeverity, null).
			addStringField("notes", null).
			addBooleanField("resolution", null);
		
		RelationshipDescriptor actToAlert = new OneToManyReferenceListDescriptor(act, alert, "alertes", true);
		RelationshipDescriptor alertToAct = new ManyToOneReferenceDescriptor(alert, act, "acte", true, true);
		alertToAct.setReverse(actToAlert, true);
		actToAlert.setReverse(alertToAct, false);
		alert.addRelationship(alertToAct);
		act.addRelationship(actToAlert);

		EntityDescriptor alertType = new EntityDescriptor("com.ockham.agora.domain", "TypeDAlerte", secured).
			addStringField("nom", null).
			addStringField("mnemonique", null).
			addBooleanField("standard", null).
			addEnumField("gravite", alertSeverity, null).
			addIntField("duree", null);
		
		alert.addRelationship(new ManyToOneReferenceDescriptor(alert, alertType, "type", true, false));
		
		alertType.addRelationship(new OneToManyReferenceListDescriptor(alertType, actType, "resoluPar", false));
		actType.addRelationship(new OneToManyReferenceListDescriptor(actType, alertType, "genere", false));

		EnumDescriptor involvedPersonalityType = new EnumDescriptor("com.ockham.agora.domain", "PersonnaliteMEC")
			.addConstant("MAJEUR", "majeur")
			.addConstant("PRE_16", "13-16")
			.addConstant("PRE_18", "13-18");
		
		EntityDescriptor alertParameter = new EntityDescriptor("com.ockham.agora.domain", "ParametreDAlerte", secured).
			addIntField("duree", null).
			addEnumField("gravite", alertSeverity, null).
			addEnumField("cadreJuridique", legal, null).
			addEnumField("regimeJuridique", legalRegime, null).
			addEnumField("personnaliteMEC", involvedPersonalityType, null);
		
		alertType.addRelationship(new OneToManyReferenceListDescriptor(alertType, alertParameter, "parametres", true));

		EnumDescriptor maritalStatus = new EnumDescriptor("com.ockham.agora.domain", "TypeNomAlt")
			.addConstant("EPOUX", "époux(se)")
			.addConstant("VEUF", "veuf(ve)")
			.addConstant("DIVORCE", "divorcé(e)")
			.addConstant("NE", "né(e)")
			.addConstant("ALIAS", "alias")
			.addConstant("DIT", "dit");

		EntityDescriptor identity = new EntityDescriptor("com.ockham.agora.domain", "Identite", secured).
			addStringField("nom", null).
			addStringField("nomAlt", null).
			addStringField("prenom", null).
			addEnumField("typeNomAlt", maritalStatus, null).
			addDateField("dateDeNaissance", null).
			addStringField("villeNaissance", null).
			addStringField("codePostalNaissance", null).
			addStringField("paysNaissance", null).
			addStringField("refDocIndentite", null).
			addDateField("dateDelivranceDocIdentite", null).
			addStringField("autoriteDelivranceDocEntite", null).
			addStringField("profession", null).
			addStringField("telephonePortable", null).
			addStringField("telephoneDomicile", null).
			addStringField("telephoneProfessionnel", null);

		EntityDescriptor location = new EntityDescriptor("com.ockham.agora.domain", "Lieu", secured).
			addStringField("precision", null);

		location.addRelationship(new OneToManyReferenceListDescriptor(location, address, "adresse", true));
				
		procedure.addRelationship(new ManyToOneReferenceDescriptor(procedure, identity, "victimePrincipale", false, false));
		procedure.addRelationship(new ManyToOneReferenceDescriptor(procedure, identity, "MECPrincipale", false, false));

		RelationshipDescriptor identityToCustody = new OneToManyReferenceListDescriptor(identity, custody, "gardesAVue", true);
		RelationshipDescriptor custodyToIdentity = new ManyToOneReferenceDescriptor(custody, identity, "identite", true, true);
		custodyToIdentity.setReverse(identityToCustody, true);
		identityToCustody.setReverse(custodyToIdentity, false);
		identity.addRelationship(identityToCustody);
		custody.addRelationship(custodyToIdentity);
		
		EnumDescriptor involvedRole = new EnumDescriptor("com.ockham.agora.domain", "RolePartiePrenante")
			.addConstant("VICTIME", "victime")
			.addConstant("MEC", "mise en cause")
			.addConstant("TEMOIN", "témoin");
			
		EntityDescriptor involvedPersonality = new EntityDescriptor("com.ockham.agora.domain", "PartiePrenante", secured)
			.addEnumField("role", involvedRole, null);
		
		RelationshipDescriptor identityToInvolvedPersonality = new OneToManyReferenceListDescriptor(identity, involvedPersonality, "actes", true);
		RelationshipDescriptor involvedPersonalityToIdentity = new ManyToOneReferenceDescriptor(involvedPersonality, identity, "identite", true, true);
		involvedPersonalityToIdentity.setReverse(identityToInvolvedPersonality, true);
		identityToInvolvedPersonality.setReverse(involvedPersonalityToIdentity, false);
		identity.addRelationship(identityToInvolvedPersonality);
		involvedPersonality.addRelationship(involvedPersonalityToIdentity);
		
		RelationshipDescriptor actToInvolvedPersonality = new OneToManyReferenceListDescriptor(act, involvedPersonality, "identites", true);
		RelationshipDescriptor involvedPersonalityToAct = new ManyToOneReferenceDescriptor(involvedPersonality, act, "acte", true, true);
		involvedPersonalityToAct.setReverse(actToInvolvedPersonality, true);
		actToInvolvedPersonality.setReverse(involvedPersonalityToAct, false);
		act.addRelationship(actToInvolvedPersonality);
		involvedPersonality.addRelationship(involvedPersonalityToAct);

		EntityDescriptor identityDocType = new EntityDescriptor("com.ockham.agora.domain", "TypeDocIdentite", secured)
			.addStringField("nom", null);
		
		identity.addRelationship(new ManyToOneReferenceDescriptor(identity, identityDocType, "typeDocIdentite", false, false));

		EntityDescriptor memento = new EntityDescriptor("com.ockham.agora.domain", "Memento", secured)
			.addIntField("ordre", null)
			.addStringField("titre", null)
			.addStringField("description", null)
			.addDateField("dateCreation", "new Date()");

		group.addRelationship(new OneToManyReferenceListDescriptor(group, memento, "mementos", true));
		
		EnumDescriptor gender = new EnumDescriptor("com.ockham.agora.dto", "Civilite")
			.addConstant("M", "M.")
			.addConstant("MME", "Mme")
			.addConstant("MLLE", "Mlle");

		EnumDescriptor judgeRankType = new EnumDescriptor("com.ockham.agora.dto", "TypeDeGradeJuge")
			.addConstant("JUGE_INSTRUCTION", "Juge d'instruction");		
		
		EntityDescriptor tribunal = new EntityDescriptor("com.ockham.agora.domain", "Tribunal", secured)
			.addStringField("nom", null);
		ValueObjectDescriptor tribunalDTO = new ValueObjectDescriptor("com.ockham.agora.dto", tribunal);
		
		EntityDescriptor language = new EntityDescriptor("com.ockham.agora.domain", "Langue", secured)
			.addStringField("nom", null);
		ValueObjectDescriptor languageDTO = new ValueObjectDescriptor("com.ockham.agora.dto", language);

		EntityDescriptor judge = new EntityDescriptor("com.ockham.agora.domain", "Juge", secured)
			.addStringField(true, 2, 60, "nom", null)
			.addStringField(true, 2, 60, "prenom", null)
			.addEnumField("civilite", gender, null)
			.addEnumField("grade", judgeRankType, null)
			.addOneToMany(tribunal, "tribunalsDeRattachement", false);
		ValueObjectDescriptor judgeDTO = new ValueObjectDescriptor("com.ockham.agora.dto", judge);

		EntityDescriptor interpretor = new EntityDescriptor("com.ockham.agora.domain", "Interprete", secured)
			.addStringField(true, 2, 60, "nom", null)
			.addStringField(true, 2, 60, "prenom", null)
			.addEnumField("civilite", gender, null)
			.addOneToMany(tribunal, "languesDEnregistrement", false)
			.addOneToMany(tribunal, "tribunalsDeRattachement", false);
		ValueObjectDescriptor interpretorDTO = new ValueObjectDescriptor("com.ockham.agora.dto", interpretor);

		EntityDescriptor lawyer = new EntityDescriptor("com.ockham.agora.domain", "Avocat", secured)
			.addStringField(true, 2, 60, "nom", null)
			.addStringField(true, 2, 60, "prenom", null)
			.addEnumField("civilite", gender, null)
			.addStringField(true, 2, 60, "barreau", null)
			.addStringField(true, 4, 16, "telephone", null);
		ValueObjectDescriptor lawyerDTO = new ValueObjectDescriptor("com.ockham.agora.dto", lawyer);
		
		EntityDescriptor assistant = new EntityDescriptor("com.ockham.agora.domain", "Assistant", secured)
			.addStringField(false, 2, 60, "nom", null)
			.addStringField(false, 2, 60, "prenom", null)
			.addEnumField("civilite", gender, null)
			.addStringField(false, 2, 10, "matricule", null)
			.addStringField(false, 2, 80, "service", null)
			.addOneToOne(user, "represente", false, false, false);
		ValueObjectDescriptor userDTO = new ValueObjectDescriptor("com.ockham.agora.dto", user);
		ValueObjectDescriptor assistantDTO = new ValueObjectDescriptor("com.ockham.agora.dto", assistant);

		EntityDescriptor commissionRogatoire = new EntityDescriptor("com.ockham.agora.domain", "CommissionRogatoire", secured)
			.addStringField(false, 2, 16, "numero", null)
			.addDateField("dateDelivrance", null)
			.addEnumField("civilite", gender, null)
			.addStringField(false, 2, 80, "contre", null)
			.addStringField(false, 2, 60, "infraction", null)
			.addManyToOne(judge, "delivreePar", true, false);
		ValueObjectDescriptor commissionRogatoireDTO = new ValueObjectDescriptor("com.ockham.agora.dto", commissionRogatoire);
/*
	// Génération
		System.out.println("********************* Role.java");
		TestUtil.println(role.getText());
		System.out.println("********************* TypeDeGrade.java");
		TestUtil.println(rankType.getText());
		System.out.println("********************* Utilisateur.java");
		TestUtil.println(user.getText());
		System.out.println("********************* Groupe.java");
		TestUtil.println(group.getText());
		System.out.println("********************* Adresse.java");
		TestUtil.println(address.getText());
		System.out.println("********************* Service.java");
		TestUtil.println(service.getText());

		System.out.println("********************* CadreJuridique.java");
		TestUtil.println(legal.getText());
		System.out.println("********************* RegimeJuridique.java");
		TestUtil.println(legalRegime.getText());
		System.out.println("********************* OrdreArticulation.java");
		TestUtil.println(sortType.getText());
		System.out.println("********************* Procedure.java");
		TestUtil.println(procedure.getText());
		System.out.println("********************* Dossier.java");
		TestUtil.println(folder.getText());
		System.out.println("********************* GardeAVue.java");
		TestUtil.println(custody.getText());
		System.out.println("********************* TypeDeDelit.java");
		TestUtil.println(offenseType.getText());
		System.out.println("********************* FamilleDeDelits.java");
		TestUtil.println(offenseFamily.getText());
		System.out.println("********************* RoleGardeAVue.java");
		TestUtil.println(involvedRole.getText());
		System.out.println("********************* PartiePrenante.java");
		TestUtil.println(involvedPersonality.getText());
	
		System.out.println("********************* StatutRedaction.java");
		TestUtil.println(editingStatus.getText());
		System.out.println("********************* Acte.java");
		TestUtil.println(act.getText());
		System.out.println("********************* ModeDeCreationDActe.java");
		TestUtil.println(actCreationMode.getText());
		System.out.println("********************* VisibiliteDActe.java");
		TestUtil.println(visibility.getText());
		System.out.println("********************* ModelDActe.java");
		TestUtil.println(actModel.getText());
		System.out.println("********************* TypeDActe.java");
		TestUtil.println(actType.getText());
		System.out.println("********************* FamilleDActes.java");
		TestUtil.println(actFamily.getText());

		System.out.println("********************* GraviteDAlerte.java");
		TestUtil.println(alertSeverity.getText());
		System.out.println("********************* VisibiliteDAlerte.java");
		TestUtil.println(alertVisibility.getText());
		System.out.println("********************* Alerte.java");
		TestUtil.println(alert.getText());
		System.out.println("********************* TypeDAlerte.java");
		TestUtil.println(alertType.getText());
		System.out.println("********************* PersonnaliteMEC.java");
		TestUtil.println(involvedPersonalityType.getText());
		System.out.println("********************* ParametreDAlerte.java");
		TestUtil.println(alertParameter.getText());
		System.out.println("********************* TypeNomAlt.java");
		TestUtil.println(maritalStatus.getText());
		System.out.println("********************* TypeDocIdentite.java");
		TestUtil.println(identityDocType.getText());
		System.out.println("********************* Identite.java");
		TestUtil.println(identity.getText());

		System.out.println("********************* Memento.java");
		TestUtil.println(memento.getText());
*/
		System.out.println("********************* Gender.java");
		TestUtil.println(gender.getText());
		System.out.println("********************* JudgeRankType.java");
		TestUtil.println(judgeRankType.getText());
		System.out.println("********************* Tribunal.java");
		TestUtil.println(tribunal.getText());
		TestUtil.println(tribunalDTO.getText());
		System.out.println("********************* Language.java");
		TestUtil.println(language.getText());
		TestUtil.println(languageDTO.getText());
		System.out.println("********************* Judge.java");
		TestUtil.println(judge.getText());
		TestUtil.println(judgeDTO.getText());
		System.out.println("********************* Interpretor.java");
		TestUtil.println(interpretor.getText());
		TestUtil.println(interpretorDTO.getText());
		System.out.println("********************* Lawyer.java");
		TestUtil.println(lawyer.getText());
		TestUtil.println(lawyerDTO.getText());
		System.out.println("********************* Assistant.java");
		TestUtil.println(assistant.getText());
		TestUtil.println(assistantDTO.getText());
		System.out.println("********************* CommissionRogatoire.java");
		TestUtil.println(commissionRogatoire.getText());
		TestUtil.println(commissionRogatoireDTO.getText());
	}
	
	public static void main(String[] args) {
		new GenerateAgora().generateEntities();
	}
}
