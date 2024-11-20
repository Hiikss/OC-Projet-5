describe('User information', () => {
  beforeEach(() => {
    cy.login(false);
  });

  it('should display user information correctly', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: "test@test.com",
        lastName: "lastname",
        firstName: "firstname",
        admin: false,
        createdAt: "2024-11-15T00:00:00",
        updatedAt: "2024-11-15T00:00:00"
      }
    });

    cy.get('span.link').contains('Account').click();
    cy.get('p').should('contain', 'Name: firstname LASTNAME');
    cy.get('p').should('contain', 'Email: test@test.com');
    cy.get('p').contains('Create at: November 15, 2024');
    cy.get('p').contains('Last update: November 15, 2024');
  });

  it('should go back with history', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: "test@test.com",
        lastName: "lastname",
        firstName: "firstname",
        admin: false,
        createdAt: "2024-11-15T00:00:00",
        updatedAt: "2024-11-15T00:00:00"
      }
    });

    cy.get('span.link').contains('Account').click();
    cy.get('button.mat-icon-button').click();

    cy.url().should('include', '/sessions');
  });

  it('should delete user account successfully', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: "test@test.com",
        lastName: "lastname",
        firstName: "firstname",
        admin: false,
        createdAt: "2024-11-15T00:00:00",
        updatedAt: "2024-11-15T00:00:00"
      }
    });

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
      body: {}
    });

    cy.get('span.link').contains('Account').click();
    cy.get('button.mat-raised-button').click();

    cy.url().should('include', '/');
    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
  });
});
