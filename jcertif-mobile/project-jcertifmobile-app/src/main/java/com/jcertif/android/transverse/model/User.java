package com.jcertif.android.transverse.model;

/**
 * 
 * @author Yakhya DABO
 *
 */

public class User {
	
	private String civilite;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private String role;
	private String typeUser;
	private String compagnie;
	private String siteWeb;
	private String telFixe;
	private String telMobile;
	private String ville;
	private String pays;
	
	public String getCivilite() {
		return civilite;
	}
	
	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getType() {
		return typeUser;
	}
	public void setType(String type) {
		this.typeUser = type;
	}
	
	public String getCompagnie() {
		return compagnie;
	}
	
	public void setCompagnie(String compagnie) {
		this.compagnie = compagnie;
	}
	
	public String getSiteWeb() {
		return siteWeb;
	}
	
	public void setSiteWeb(String siteWeb) {
		this.siteWeb = siteWeb;
	}
	
	public String getTelFixe() {
		return telFixe;
	}
	
	public void setTelFixe(String telephoneFixe) {
		this.telFixe = telephoneFixe;
	}
	
	public String getTelMobile() {
		return telMobile;
	}
	
	public void setTelMobile(String telephoneMobile) {
		this.telMobile = telephoneMobile;
	}
	
	public String getVille() {
		return ville;
	}
	
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public String getPays() {
		return pays;
	}
	
	public void setPays(String pays) {
		this.pays = pays;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 *
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [civilite=");
		builder.append(civilite);
		builder.append(", nom=");
		builder.append(nom);
		builder.append(", prenom=");
		builder.append(prenom);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append(", role=");
		builder.append(role);
		builder.append(", type=");
		builder.append(typeUser);
		builder.append(", compagnie=");
		builder.append(compagnie);
		builder.append(", siteWeb=");
		builder.append(siteWeb);
		builder.append(", telephoneFixe=");
		builder.append(telFixe);
		builder.append(", telephoneMobile=");
		builder.append(telMobile);
		builder.append(", ville=");
		builder.append(ville);
		builder.append(", pays=");
		builder.append(pays);
		return builder.toString();
	}*/
}
