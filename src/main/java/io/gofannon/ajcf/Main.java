package io.gofannon.ajcf;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;
import org.eclipse.text.edits.UndoEdit;

public class Main {
    public static void main(String[] args) {
        String source = """
                package org.example.stuff;
                                
                public class Toto {
                    public String sayHello() {
                        return "Hello";
                    }
                }
                """;

//        Document document = new Document(source);
//
//        ASTParser parser = ASTParser.newParser(AST.JLS17);
//        parser.setSource(document.get().toCharArray());
//        parser.setKind(ASTParser.K_COMPILATION_UNIT);
//
//        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//        AST ast = cu.getAST();
//        ASTRewrite rewriter = ASTRewrite.create(ast);

        //cu.recordModifications();

        stuff2();
    }

    public static void stuff() {
        Document document = new Document("""
                import java.util.List;
                
                /**
                 * This is an anonymous class
                 */
                class X {
                    // fzefze
                    public String sayHello() {
                        return    
                              
                              
                                    "Hello" ;
                    }
                }
                """);
        ASTParser parser = ASTParser.newParser(AST.JLS17);
        parser.setSource(document.get().toCharArray());
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        AST ast = cu.getAST();
        ImportDeclaration id = ast.newImportDeclaration();
        id.setName(ast.newName(new String[]{"java", "util", "Set"}));
        ASTRewrite rewriter = ASTRewrite.create(ast);
        TypeDeclaration td = (TypeDeclaration) cu.types().get(0);

        ListRewrite typeListRewrite  = rewriter.getListRewrite(cu,CompilationUnit.TYPES_PROPERTY);
        TypeDeclaration sourceNode = (TypeDeclaration)typeListRewrite.getOriginalList().get(0);
        System.out.println("===>"+sourceNode.getLength());

        //inType.getName().setIdentifier("X2");
        TypeDeclaration editedNode = (TypeDeclaration)typeListRewrite.createCopyTarget(sourceNode, sourceNode);
        System.out.println("===>"+editedNode.getLength());
        typeListRewrite.replace(sourceNode, editedNode, null);

        //ITrackedNodePosition tdLocation = rewriter.track(td);
//        ListRewrite lrw = rewriter.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
//        lrw.insertLast(id, null);
        TextEdit edits = rewriter.rewriteAST(document, null);
        UndoEdit undo = null;
        try {
            undo = edits.apply(document);
        } catch (MalformedTreeException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        System.out.println("Content : ");
        System.out.println(document.get());

        //assert "import java.util.List;\nimport java.util.Set;\nclass X {}\n".equals(document.get());
        // tdLocation.getStartPosition() and tdLocation.getLength()
        // are new source range for "class X {}" in document.get(
    }


    public static void stuff2() {
        Document document = new Document("""
                import java.util.List;
                
                /**
                 * This is an anonymous class
                 */
                class X {
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
        TypeDeclaration typeDeclaration = (TypeDeclaration)cu.types().get(0);
        SimpleName newName = cu.getAST().newSimpleName("Y");
        typeDeclaration.setName(newName);

        // computation of the text edits
        TextEdit edits = cu.rewrite(document,null);



        try {
             edits.apply(document);
        } catch (MalformedTreeException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        System.out.println("Content : ");
        System.out.println(document.get());

        //assert "import java.util.List;\nimport java.util.Set;\nclass X {}\n".equals(document.get());
        // tdLocation.getStartPosition() and tdLocation.getLength()
        // are new source range for "class X {}" in document.get(
    }

}