import { Link, useLocation } from "react-router-dom";
import { useAuthStore } from "../store/useAuthStore.js";
import { useCartStore } from "../store/useCartStore.js";
import { useEffect } from "react";
import { LogOut, ShoppingBag, ShoppingCart, User, Package, Shield } from "lucide-react";

const Navbar = () => {
    const { authUser, logout } = useAuthStore();
    const { totalItems, fetchCart, resetCart } = useCartStore();
    const location = useLocation();

    // Завантажуємо кошик при авторизації
    useEffect(() => {
        if (authUser) {
            fetchCart();
        } else {
            resetCart();
        }
    }, [authUser, fetchCart, resetCart]);

    const handleLogout = () => {

        resetCart(); // Очищаємо кошик перед логаутом
        logout();
    };

    return (
        <nav className="bg-white shadow-lg border-b border-gray-200">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center">
                        <Link to={authUser ? "/home" : "/"} className="flex items-center space-x-2">
                            <ShoppingBag className="h-8 w-8 text-blue-600" />
                            <span className="text-xl font-bold text-gray-900">E-Store</span>
                        </Link>
                    </div>

                    <div className="flex items-center space-x-4">
                        {authUser ? (
                            // Authenticated user menu
                            <>
                                {/* Navigation Links */}
                                <div className="hidden md:flex items-center space-x-4">
                                    <Link
                                        to="/home"
                                        className={`px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                            location.pathname === "/home"
                                                ? "bg-blue-100 text-blue-700"
                                                : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                        }`}
                                    >
                                        Products
                                    </Link>
                                    <Link
                                        to="/orders"
                                        className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                            location.pathname === "/orders"
                                                ? "bg-blue-100 text-blue-700"
                                                : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                                        }`}
                                    >
                                        <Package className="h-4 w-4 mr-1" />
                                        Orders
                                    </Link>

                                    {/* Admin Navigation */}
                                    {authUser.isAdmin && (
                                        <Link
                                            to="/admin/articles"
                                            className={`flex items-center px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                                location.pathname === "/admin/articles"
                                                    ? "bg-purple-100 text-purple-700"
                                                    : "text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                                            }`}
                                        >
                                            <Shield className="h-4 w-4 mr-1" />
                                            Admin Panel
                                        </Link>
                                    )}
                                </div>

                                {/* Cart Icon */}
                                <Link
                                    to="/cart"
                                    className="relative p-2 text-gray-500 hover:text-gray-700 transition-colors"
                                >
                                    <ShoppingCart className="h-6 w-6" />
                                    {totalItems > 0 && (
                                        <span className="absolute -top-1 -right-1 bg-blue-600 text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                                            {totalItems > 99 ? "99+" : totalItems}
                                        </span>
                                    )}
                                </Link>

                                {/* User Menu */}
                                <div className="flex items-center space-x-3">
                                    <div className="flex items-center space-x-1">
                                        <User className="h-5 w-5 text-gray-500" />
                                        <span className="text-sm font-medium text-gray-700 hidden sm:block">
                                            {authUser.name}
                                        </span>
                                        {authUser.isAdmin && (
                                            <span className="ml-1 inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-purple-100 text-purple-800">
                                                Admin
                                            </span>
                                        )}
                                    </div>

                                    <button
                                        onClick={handleLogout}
                                        className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-gray-900 hover:bg-gray-100 rounded-md transition-colors"
                                    >
                                        <LogOut className="h-4 w-4 mr-1" />
                                        Logout
                                    </button>
                                </div>
                            </>
                        ) : (
                            // Unauthenticated user menu
                            <div className="flex items-center space-x-2">
                                <Link
                                    to="/login"
                                    className="px-4 py-2 text-sm font-medium text-gray-700 hover:text-gray-900 transition-colors"
                                >
                                    Login
                                </Link>
                                <Link
                                    to="/register"
                                    className="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors"
                                >
                                    Sign Up
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            {/* Mobile Navigation Menu - только для мобильных устройств */}
            {authUser && (
                <div className="md:hidden border-t border-gray-200 bg-gray-50">
                    <div className="px-4 py-2 space-y-1">
                        <Link
                            to="/home"
                            className={`block px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                location.pathname === "/home"
                                    ? "bg-blue-100 text-blue-700"
                                    : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                            }`}
                        >
                            Products
                        </Link>
                        <Link
                            to="/orders"
                            className={`block px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                location.pathname === "/orders"
                                    ? "bg-blue-100 text-blue-700"
                                    : "text-gray-700 hover:text-gray-900 hover:bg-gray-100"
                            }`}
                        >
                            Orders
                        </Link>
                        {authUser.isAdmin && (
                            <Link
                                to="/admin/articles"
                                className={`block px-3 py-2 rounded-md text-sm font-medium transition-colors ${
                                    location.pathname === "/admin/articles"
                                        ? "bg-purple-100 text-purple-700"
                                        : "text-purple-600 hover:text-purple-700 hover:bg-purple-50"
                                }`}
                            >
                                Admin Panel
                            </Link>
                        )}
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;