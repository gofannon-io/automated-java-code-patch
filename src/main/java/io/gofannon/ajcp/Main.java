package io.gofannon.ajcp;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

public class Main {
    public static void main(String[] args) throws BadLocationException {
        Document document = new Document("""
                import java.util.List;
                                
                /**
                 * This is an anonymous class
                 */
                class OldName {
                    // fzefze
                    public String sayHello() {
                        return    
                                "Hello, John" ;
                    }
                }
                """);
        ASTParser parser = ASTParser.newParser(AST.JLS17);
        parser.setSource(document.get().toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        AST ast = cu.getAST();

        cu.recordModifications();
        TypeDeclaration typeDeclaration = (TypeDeclaration) cu.types().get(0);
        SimpleName newName = cu.getAST().newSimpleName("NewName");
        typeDeclaration.setName(newName);

        // computation of the text edits
        TextEdit edits = cu.rewrite(document, null);


        edits.apply(document);

        System.out.println("Content : ");
        System.out.println(document.get());
    }

}